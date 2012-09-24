package org.vbossica.sample;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.vbossica.springbox.cliapp.Module;
import org.vbossica.springbox.cliapp.ModuleConfiguration;

/**
 * @author vladimir
 */
@ModuleConfiguration( name = "SampleTool", description = "sample module" )
public class SampleModule implements Module {

  @Override
  public Options getOptions() {
    return new Options()
        .addOption( OptionBuilder.hasArg().withArgName( "id" ).withLongOpt( "solution" ).create( 's' ) );
  }

  @Override
  public void process( final CommandLine cmd ) {
    System.out.println( "processing" );
    if ( cmd.hasOption( 's' ) ) {
      System.out.println( "using solution " + cmd.getOptionValue( 's' ) );
    }
  }

}
