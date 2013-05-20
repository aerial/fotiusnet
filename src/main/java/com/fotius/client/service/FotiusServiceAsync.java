package com.fotius.client.service;

import com.fotius.shared.model.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;

public interface FotiusServiceAsync {

    void saveTeacher(Teacher teacher, AsyncCallback<Teacher> async);

    void saveStudent(Student student, AsyncCallback<Student> async);

    void removeTeacher(Teacher teacher, AsyncCallback<Void> async);

    void removeStudent(Student student, AsyncCallback<Void> async);

    void getTeachers(PagingLoadConfig config, AsyncCallback<PagingLoadResult<Teacher>> async);

    void loginAsTeacher(String login, String password, AsyncCallback<Teacher> async);

    void loginAsStudent(String login, String password, AsyncCallback<Student> async);

    void getStudents(PagingLoadConfig config, AsyncCallback<PagingLoadResult<Student>> async);

    void getTeacherRoles(PagingLoadConfig config, AsyncCallback<PagingLoadResult<TeacherRole>> async);

    void getStudentRoles(PagingLoadConfig config, AsyncCallback<PagingLoadResult<StudentRole>> async);

    void getStudentGroups(PagingLoadConfig config, AsyncCallback<PagingLoadResult<StudentGroup>> async);

    void saveStudentGroup(StudentGroup group, AsyncCallback<StudentGroup> async);

    void removeGroup(StudentGroup group, AsyncCallback<Void> async);
}
