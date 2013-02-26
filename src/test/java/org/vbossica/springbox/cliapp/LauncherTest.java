package org.vbossica.springbox.cliapp;

import org.junit.Test;

/**
 * @author vladimir
 */
public class LauncherTest {

  @Test
  public void shouldShowHelp() {
    String[] args = new String[]{ "-h" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldNotInitializeWrongModule() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "WrongModule", "-s", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldInitializeSampleModule() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "SampleModule", "-s", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldNotInitializeModuleWithWrongArgument() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "SampleModule", "-r", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldInitializeSampleSpringModule() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--module", "SampleSpringModule", "-a", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldListModules() {
    String[] args = new String[]{ "--package", "org.vbossica.sample", "--list" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldListModulesWithoutPackage() {
    String[] args = new String[]{ "--package", "", "--list" };
    ModuleLauncher.main( args );
  }

}
