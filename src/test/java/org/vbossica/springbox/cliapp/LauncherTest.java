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
  public void shouldNotInitializeWrongTool() {
    String[] args = new String[]{ "-package", "org.vbossica", "-tool", "WrongTool", "-s", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldInitializeSampleTool() {
    String[] args = new String[]{ "-package", "org.vbossica", "-tool", "SampleTool", "-s", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldInitializeSampleTool2() {
    String[] args = new String[]{ "-package", "org.vbossica", "-tool", "SampleTool", "-r", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldInitializeSampleSpringTool() {
    String[] args = new String[]{ "-package", "org.vbossica", "-tool", "SampleSpringTool", "-s", "123" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldListTools() {
    String[] args = new String[]{ "-package", "org.vbossica", "-list" };
    ModuleLauncher.main( args );
  }

  @Test
  public void shouldListToolsWithoutPackage() {
    String[] args = new String[]{ "-package", "", "-list" };
    ModuleLauncher.main( args );
  }

}
