import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ScheduleTest {
  
  Course testCourse;
  
  @Before
  public void makeCourse() {
    testCourse = new Course(1, 1, "dep", 'A', "KET", "test class", "test", "this is a test course", "Professor", "mwf", "11:00:00", "12:00:00", 19, 10, "300A", null);
  }

  public void testGetSchedule() {
    Schedule schedule = new Schedule();
    assertNotNull(schedule.getSchedule());
  }

  @Test
  public void testAdd() {
    Schedule schedule = new Schedule();
    schedule.add(testCourse);
    assertTrue(schedule.getSchedule().size() == 1);
  }

  @Test
  public void testHasSchedule() {
    Schedule schedule = new Schedule();
    assertFalse(schedule.hasSchedule());
  }


  // not testable here, pings db
  //@Test
  public void testRetrieveSchedule() {

  }

  //not testable here, pings db
  //@Test
  public void testSaveSchedule() {

  }
}
