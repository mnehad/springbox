package org.vbossica.sample;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.vbossica.springbox.cliapp.Module;
import org.vbossica.springbox.cliapp.ModuleConfiguration;

/**
 * @author vladimir
 */
@SuppressWarnings("UnusedDeclaration")
@ModuleConfiguration( name = "SampleModule", description = "sample module" )
public class SampleModule implements Module {

  @Override
  public Options getOptions() {
    return new Options()
        .addOption( OptionBuilder.hasArg().withArgName( "value" ).withLongOpt( "argument" ).create( 'a' ) );
  }

  @Override
  public void process( final CommandLine cmd ) {
    System.out.println( "processing" );
    if ( cmd.hasOption( 'a' ) ) {
      System.out.println( "using argument " + cmd.getOptionValue( 'a' ) );
    }
  }

}
