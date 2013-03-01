/*
 * Copyright 2012-2013 the original author or authors.
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
import org.apache.commons.lang.Validate;
import org.reflections.Reflections;

/**
 * Entry-point command line application that automatically discovers modules in the classpath and let individual ones
 * be executed. To register a module:
 *
 * <ol>
 *   <li>Create a class that implements the {@link Module} interface</li>
 *   <li>Annotate the class with {@link ModuleConfiguration}</li>
 *   <li>Place the compiles class in the classpath</li>
 *   <li>Execute {@code org.vbossica.springbox.cliapp.ModuleLauncher --package [package_name] --list} to get the
 *   list of modules present in the package {@code package_name}</li>
 *   <li>Execute {@code org.vbossica.springbox.cliapp.ModuleLauncher --package [package_name] --module [module_name]} to
 *   execute one registered module</li>
 * </ol>
 *
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

  /**
   * Explores the classpath and returns all the classes with the {@link ModuleConfiguration} annotation.
   */
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
        .addOption( OptionBuilder.withLongOpt( HELP_OPTION ).withDescription("shows this help").create( 'h' ) )
        .addOption( OptionBuilder.hasArg().withArgName("name").withLongOpt(PACKAGE_OPTION).withDescription("name of the package to scan").create( 'p' ) )
        .addOption( OptionBuilder.withLongOpt( LIST_OPTION ).withDescription("lists all registered modules").create( 'l' ) )
        .addOption( OptionBuilder.hasArg().withArgName( "name" ).withLongOpt( MODULE_OPTION ).withDescription("name of the module to execute").create('m') );

    String packageName = null;

    try {
      CommandLine cmd = new PosixParser().parse( options, args, true );
      if ( cmd.hasOption( HELP_OPTION ) && !cmd.hasOption( MODULE_OPTION )) {
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
    Validate.notEmpty(packageName, "required package must be set");

    out.println( "Registered modules:" );
    for ( ModuleConfig module : findModules( packageName ).values() ) {
      out.println( "  " + module.name + " - " + module.description );
    }
  }

  private void initializeTool( final String packageName, final CommandLine cmd, final String moduleName )
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    Validate.notEmpty(packageName, "required package must be set");

    Map<String, ModuleConfig> configs = findModules( packageName );
    ModuleConfig moduleConfig = configs.get( moduleName );
    if ( null == moduleConfig || null == moduleConfig.className ) {
      throw new IllegalArgumentException( "module couldn't be found: " + moduleName );
    }
    Module module = (Module) Class.forName( moduleConfig.className ).newInstance();
    Options options = module.getOptions();
    if ( cmd.hasOption( HELP_OPTION ) ) {
        printModuleHelp(moduleConfig, options);
        return;
    }
    try {
      CommandLine subCmd = new PosixParser().parse( options, cmd.getArgs() );
      module.process(subCmd);
    } catch ( Exception ex ) {
      System.err.println( ex.getMessage() );
        printModuleHelp(moduleConfig, options);
    }
  }

    private void printModuleHelp(ModuleConfig moduleConfig, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "java " + ModuleLauncher.class.getName() + " --module " + moduleConfig.name, options, true );
    }

    private void printHelp( final Options options ) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "java " + ModuleLauncher.class.getName(), options, true );
  }

}
