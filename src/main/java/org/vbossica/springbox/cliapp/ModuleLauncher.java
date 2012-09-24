/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vbossica.springbox.cliapp;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.reflections.Reflections;

/**
 * @author vladimir
 */
public class ModuleLauncher {

  private static final String HELP_OPTION = "help";
  private static final String PACKAGE_OPTION = "package";
  private static final String LIST_OPTION = "list";
  private static final String MODULE_OPTION = "module";

  public static void main( String[] args ) {
    new ModuleLauncher().process( args );
  }

  private Map<String, ModuleConfig> findModules( String pkgName ) {
    Map<String, ModuleConfig> result = Maps.newHashMap();

    Set<Class<?>> annotated = new Reflections( pkgName ).getTypesAnnotatedWith( ModuleConfiguration.class );
    for ( Class<?> cls : annotated ) {
      ModuleConfiguration annotation = cls.getAnnotation( ModuleConfiguration.class );
      ModuleConfig config = new ModuleConfig();
      config.name = annotation.name();
      config.description = annotation.description();
      config.className = cls.getName();

      result.put( config.name, config );
    }
    return result;
  }

  private final static class ModuleConfig {
    String name;
    String description;
    String className;
  }

  @SuppressWarnings( "AccessStaticViaInstance" )
  private void process( final String[] args ) {
    Options options = new Options()
        .addOption( OptionBuilder.withLongOpt( HELP_OPTION ).create( 'h' ) )
        .addOption(
            OptionBuilder.hasArg().withArgName( "name" ).withLongOpt( PACKAGE_OPTION ).isRequired().create( 'p' ) )
        .addOption( OptionBuilder.withLongOpt( LIST_OPTION ).create( 'l' ) )
        .addOption( OptionBuilder.hasArg().withArgName( "class" ).withLongOpt( MODULE_OPTION ).create( 'm' ) );

    String packageName = null;

    try {
      CommandLine cmd = new PosixParser().parse( options, args, true );
      if ( cmd.hasOption( HELP_OPTION ) ) {
        printHelp( options );
        return;
      }
      if ( cmd.hasOption( PACKAGE_OPTION ) ) {
        packageName = cmd.getOptionValue( PACKAGE_OPTION );
      }
      if ( cmd.hasOption( LIST_OPTION ) ) {
        listModules( packageName, System.out );
        return;
      }
      if ( cmd.hasOption( MODULE_OPTION ) ) {
        String tool = cmd.getOptionValue( MODULE_OPTION );
        initializeTool( packageName, cmd, tool );
      } else {
        System.err.println( "missing module definition" );
      }
    } catch ( Exception ex ) {
      System.err.println( ex.getMessage() );
      printHelp( options );
    }
  }

  private void listModules( final String packageName, PrintStream out ) {
    out.println( "Registered tools:" );
    for ( ModuleConfig module : findModules( packageName ).values() ) {
      out.println( "  " + module.name + " - " + module.description );
    }
  }

  private void initializeTool( final String packageName, final CommandLine cmd, final String moduleName )
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    Map<String, ModuleConfig> configs = findModules( packageName );
    ModuleConfig module = configs.get( moduleName );
    if ( null == module ) {
      throw new IllegalArgumentException( "module couldn't be found: " + moduleName );
    }
    if ( null == module.className ) {
      throw new IllegalArgumentException( "module couldn't be found: " + moduleName );
    }
    Module tool = (Module) Class.forName( module.className ).newInstance();
    Options options = tool.getOptions();
    try {
      CommandLine subCmd = new PosixParser().parse( options, cmd.getArgs() );
      tool.process( subCmd );
    } catch ( Exception ex ) {
      System.err.println( ex.getMessage() );
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp( "java " + ModuleLauncher.class.getName() + " -tool " + module.className, options, true );
    }
  }

  private void printHelp( final Options options ) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "java " + ModuleLauncher.class.getName(), options, true );
  }

}
