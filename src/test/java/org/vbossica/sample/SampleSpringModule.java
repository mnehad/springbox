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
@ModuleConfiguration( name = "SampleSpringTool", description = "sample spring module" )
public class SampleSpringModule extends AbstractSpringModule {

  @Override
  public Options getOptions() {
    return new Options()
        .addOption( OptionBuilder.hasArg().withArgName( "id" ).withLongOpt( "solution" ).create( 's' ) );
  }

  @Override
  protected void doProcess( final CommandLine cmd, ApplicationContext context ) {
    if ( cmd.hasOption( 's' ) ) {
      System.out.println( "using solution " + cmd.getOptionValue( 's' ) );
    }
  }

}
