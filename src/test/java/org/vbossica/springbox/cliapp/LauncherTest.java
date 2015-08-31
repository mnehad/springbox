package org.vbossica.springbox.cliapp;

import org.junit.Test;

/**
 * @author vladimir
 */
public class LauncherTest {

  @Test
  public void shouldShowHelp() {
    String[] args = new String[]{ "--help" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldListModules() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--list" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldNotInitializeWrongModule() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "WrongModule", "-s", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldNotInitializeInvalidModule() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "TheSampleInvalidModule", "-s", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldInitializeSampleModule() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "TheSampleModule", "-a", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldNotInitializeModuleWithWrongArgument() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "TheSampleModule", "-r", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldInitializeSampleSpringModule() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "TheSampleSpringModule", "-a", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldNotInitializeModuleWithoutPackage() {
    String[] args = new String[]{ "--module", "TheSampleSpringModule", "-a", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldListModulesWithoutPackage() {
    String[] args = new String[]{ "--package", "", "--list" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldDisplayModuleHelp() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "TheSampleSpringModule", "--help" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldDisplayModuleHelpWithoutOptions() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "TheSampleModuleWithoutOptions", "--help" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldLaunchSpringModuleWithProperty() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "TheSampleSpringFailModule" };
    ModuleLauncher.main( args );
  }

}
