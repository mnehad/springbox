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
@ModuleConfiguration( name = "TheSampleModuleWithoutOptions", description = "sample module without options" )
public class SampleModuleWithoutOptions implements Module {

  @Override
  public Options getOptions() {
    return null;
  }

  @Override
  public void process( final CommandLine cmd ) {
    System.out.println( "processing" );
  }

}
