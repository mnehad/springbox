package org.vbossica.sample;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.springframework.context.ApplicationContext;
import org.vbossica.springbox.cliapp.AbstractSpringModule;
import org.vbossica.springbox.cliapp.ModuleConfiguration;

/**
 * @author vladimir
 */
@SuppressWarnings("UnusedDeclaration")
@ModuleConfiguration( name = "SampleSpringModule", description = "sample spring module" )
public class SampleSpringModule extends AbstractSpringModule {

  @Override
  public Options getOptions() {
    return new Options()
        .addOption( OptionBuilder.hasArg().withArgName( "number" ).withLongOpt( "argument" ).create( 'a' ) );
  }

  @Override
  protected void doProcess( final CommandLine cmd, ApplicationContext context ) {
    if ( cmd.hasOption( 'a' ) ) {
      System.out.println( "using argument " + cmd.getOptionValue( 'a' ) );
    }
  }

}
