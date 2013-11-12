package com.fotius.client.service;

import com.fotius.shared.model.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;

@RemoteServiceRelativePath("fotius")
public interface FotiusService extends RemoteService {

    User login(String login, String password);

    User register(User user);

    PagingLoadResult<Teacher> getTeachers(PagingLoadConfig config);

    PagingLoadResult<Student> getStudents(PagingLoadConfig config);

    List<User> getUsers();

    PagingLoadResult<StudentGroup> getStudentGroups(PagingLoadConfig config);

    StudentGroup saveStudentGroup(StudentGroup group);

    void removeGroup(StudentGroup group);

    Teacher saveTeacher(Teacher teacher);

    Student saveStudent(Student student);

    void removeTeacher(Teacher teacher);

    void removeStudent(Student student);

    PagingLoadResult<TeacherRole> getTeacherRoles(PagingLoadConfig config);

    PagingLoadResult<StudentRole> getStudentRoles(PagingLoadConfig config);

    Message sendMessage(Message msg);

    PagingLoadResult<Message> getInbox(User user, PagingLoadConfig config);

    PagingLoadResult<Message> getOutbox(User user, PagingLoadConfig config);
}
