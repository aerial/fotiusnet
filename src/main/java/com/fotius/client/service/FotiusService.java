package com.fotius.client.service;

import com.fotius.shared.model.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;

@RemoteServiceRelativePath("fotius")
public interface FotiusService extends RemoteService {

    Teacher loginAsTeacher(String login, String password);

    Student loginAsStudent(String login, String password);

    User login(String login, String password);

    PagingLoadResult<Teacher> getTeachers(PagingLoadConfig config);

    PagingLoadResult<Student> getStudents(PagingLoadConfig config);

    PagingLoadResult<StudentGroup> getStudentGroups(PagingLoadConfig config);

    StudentGroup saveStudentGroup(StudentGroup group);

    void removeGroup(StudentGroup group);

    Teacher saveTeacher(Teacher teacher);

    Student saveStudent(Student student);

    void removeTeacher(Teacher teacher);

    void removeStudent(Student student);

    PagingLoadResult<TeacherRole> getTeacherRoles(PagingLoadConfig config);

    PagingLoadResult<StudentRole> getStudentRoles(PagingLoadConfig config);
}
