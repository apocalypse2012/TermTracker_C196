package com.example.TermTracker.utilities;

import com.example.TermTracker.database.NoteEntity;
import com.example.TermTracker.database.TermEntity;
import com.example.TermTracker.database.CourseEntity;
import com.example.TermTracker.database.AssessmentEntity;
import com.example.TermTracker.database.MentorEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleData {

    private static final String SAMPLE_TEXT_1 = "A simple note";
    private static final String SAMPLE_TEXT_2 = "A note with a\nline feed";
    private static final String SAMPLE_TEXT_3 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n\n" +
            "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?";

    private static final String TITLE_TERM_TEXT_1 = "Term 1";
    private static final String TITLE_TERM_TEXT_2 = "Term 2";
    private static final String TITLE_TERM_TEXT_3 = "Term 3";

    private static final String TITLE_COURSE_TEXT_1 = "Course 1";
    private static final String TITLE_COURSE_TEXT_2 = "Course 2";
    private static final String TITLE_COURSE_TEXT_3 = "Course 3";

    private static final String TITLE_ASSESSMENT_TEXT_1 = "Assessment 1";
    private static final String TITLE_ASSESSMENT_TEXT_2 = "Assessment 2";
    private static final String TITLE_ASSESSMENT_TEXT_3 = "Assessment 3";

    private static final String DATE_TEXT_1 = "2019-04-01";
    private static final String DATE_TEXT_2 = "2019-11-01";
    private static final String DATE_TEXT_3 = "2020-04-01";
    private static final String DATE_TEXT_4 = "2020-11-01";

    private static final String NAME_TEXT_1 = "Al Beardsley";
    private static final String NAME_TEXT_2 = "Bill Carlisle";
    private static final String NAME_TEXT_3 = "Craig Dodson";

    private static final String PHONE_TEXT_1 = "(123) 456-7890";
    private static final String PHONE_TEXT_2 = "(234) 567-8901";
    private static final String PHONE_TEXT_3 = "(345) 678-9012";

    private static final String EMAIL_TEXT_1 = "al@beardsley.com";
    private static final String EMAIL_TEXT_2 = "bill@carlisle.com";
    private static final String EMAIL_TEXT_3 = "craig@dodson.com";

    private static final String STATUS_TEXT_1 = "In Progress";
    private static final String STATUS_TEXT_2 = "Dropped";
    private static final String STATUS_TEXT_3 = "Completed";



    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, diff);
        return cal.getTime();
    }

    public static List<NoteEntity> getNotes(int assessment) {
        List<NoteEntity> notes = new ArrayList<>();
        notes.add(new NoteEntity(getDate(0), SAMPLE_TEXT_1, assessment));
        notes.add(new NoteEntity(getDate(-1), SAMPLE_TEXT_2, assessment));
        notes.add(new NoteEntity(getDate(-2), SAMPLE_TEXT_3, assessment));
        return notes;
    }

    public static List<TermEntity> getTerms() {
        List<TermEntity> terms = new ArrayList<>();
        terms.add(new TermEntity(DATE_TEXT_1, DATE_TEXT_2, TITLE_TERM_TEXT_1));
        terms.add(new TermEntity(DATE_TEXT_2, DATE_TEXT_3, TITLE_TERM_TEXT_2));
        terms.add(new TermEntity(DATE_TEXT_3, DATE_TEXT_4, TITLE_TERM_TEXT_3));
        return terms;
    }

    public static List<CourseEntity> getCourses(int term) {
        List<CourseEntity> courses = new ArrayList<>();
        courses.add(new CourseEntity(DATE_TEXT_1, DATE_TEXT_2, TITLE_COURSE_TEXT_1, STATUS_TEXT_1, term));
        courses.add(new CourseEntity(DATE_TEXT_2, DATE_TEXT_3, TITLE_COURSE_TEXT_2, STATUS_TEXT_2, term));
        courses.add(new CourseEntity(DATE_TEXT_3, DATE_TEXT_4, TITLE_COURSE_TEXT_3, STATUS_TEXT_3, term));
        return courses;
    }

    public static List<AssessmentEntity> getAssessments(int course) {
        List<AssessmentEntity> assessments = new ArrayList<>();
        assessments.add(new AssessmentEntity(DATE_TEXT_2, TITLE_ASSESSMENT_TEXT_1, course));
        assessments.add(new AssessmentEntity(DATE_TEXT_3, TITLE_ASSESSMENT_TEXT_2, course));
        assessments.add(new AssessmentEntity(DATE_TEXT_4, TITLE_ASSESSMENT_TEXT_3, course));
        return assessments;
    }

    public static List<MentorEntity> getMentors(int course) {
        List<MentorEntity> assessments = new ArrayList<>();
        assessments.add(new MentorEntity(NAME_TEXT_1, PHONE_TEXT_1, EMAIL_TEXT_1, course));
        assessments.add(new MentorEntity(NAME_TEXT_2, PHONE_TEXT_2, EMAIL_TEXT_2, course));
        assessments.add(new MentorEntity(NAME_TEXT_3, PHONE_TEXT_3, EMAIL_TEXT_3, course));
        return assessments;
    }
}
