package com.fotius.server.impl;

import com.fotius.client.service.FotiusService;
import com.fotius.server.impl.hibernate.HibernateUtil;
import com.fotius.shared.model.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.*;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

public class FotiusServiceImpl extends RemoteServiceServlet
        implements FotiusService {
    private HibernateUtil hibernateUtil = new HibernateUtil();

    @Override
    public Teacher loginAsTeacher(String login, String password) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String sql = " from Teacher where login = '" + login + "' and password = '" + password + "'";

        Query query = session.createQuery(sql);
        if (query.list().size() == 0) {
            return null;
        } else {
            return (Teacher)query.list().get(0);
        }
    }

    @Override
    public Student loginAsStudent(String login, String password) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String sql = " from Student where login = " + login + " and password = " + password;

        Query query = session.createQuery(sql);
        if (query.list().size() == 0) {
            return null;
        } else {
            return (Student)query.list().get(0);
        }
    }


    @Override
    public PagingLoadResult<Teacher> getTeachers(PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String sql = " from Teacher ";
        Query query = session.createQuery(sql);
//        query.setFirstResult(start);
//        query.setMaxResults(limit);
        List<Teacher> records = new ArrayList<Teacher>(query.list());
        int start = config.getOffset();
        int limit = records.size();

        //TODO add sorting
//        ArrayList<Teacher> sublist = new ArrayList<Teacher>();
//        if (config.getLimit() > 0) {
//            limit = Math.min(start + config.getLimit(), limit);
//        }
//        if (start + config.getLimit() > records.size()) limit = records.size();
//        for (int i = config.getOffset(); i < limit; i++) {
//            sublist.add(records.get(i));
//        }

        if (config.getSortInfo().size() > 0) {
            SortInfo sort = config.getSortInfo().get(0);
            if (sort.getSortField() != null) {
                final String sortField = sort.getSortField();
                if (sortField != null) {

                    Collections.sort(records, sort.getSortDir().comparator(new Comparator<Teacher>() {
                        public int compare(Teacher t1, Teacher t2) {
                            if (sortField.equals("name")) {
                                return t1.getName().compareTo(t2.getName());
                            } else if (sortField.equals("login")) {
                                return t1.getLogin().compareTo(t2.getLogin());
                            }
                            return 0;
                        }
                    }));
                }
            }
        }

        ArrayList<Teacher> sublist = new ArrayList<Teacher>();

        if (config.getLimit() > 0) {
            limit = Math.min(start + config.getLimit(), limit);
        }
        for (int i = config.getOffset(); i < limit; i++) {
            sublist.add(records.get(i));
        }
        session.getTransaction().commit();
        return new PagingLoadResultBean<Teacher>(sublist, records.size(), config.getOffset());
    }

    @Override
    public PagingLoadResult<Student> getStudents(PagingLoadConfig config) {
//        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//        String sql = " from Student ";
//        Query query = session.createQuery(sql);
//        int start = config.getOffset();
//        int limit = config.getLimit();
//        query.setFirstResult(start);
//        query.setMaxResults(limit);
//        List<Student> records = new ArrayList<Student>(query.list());
//        //TODO add sorting
//        ArrayList<Student> sublist = new ArrayList<Student>();
//        if (config.getLimit() > 0) {
//            limit = Math.min(start + config.getLimit(), limit);
//        }
//        if (start + config.getLimit() > records.size()) limit = records.size();
//        for (int i = config.getOffset(); i < limit; i++) {
//            sublist.add(records.get(i));
//        }
//        session.getTransaction().commit();
//        return new PagingLoadResultBean<Student>(sublist, records.size(), config.getOffset());
//



        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String sql = " from Student ";
        final Query query = session.createQuery(sql);
        List<Student> result = query.list();

        int start = config.getOffset();
        int limit = result.size();

        if (config.getSortInfo().size() > 0) {
            SortInfo sort = config.getSortInfo().get(0);
            if (sort.getSortField() != null) {
                final String sortField = sort.getSortField();
                if (sortField != null) {

                    Collections.sort(result, sort.getSortDir().comparator(new Comparator<Student>() {
                        public int compare(Student s1, Student s2) {
                            if (sortField.equals("name")) {
                                return s1.getName().compareTo(s2.getName());
                            } else if (sortField.equals("login")) {
                                return s1.getLogin().compareTo(s2.getLogin());
                            }
                            return 0;
                        }
                    }));
                }
            }
        }

        ArrayList<Student> sublist = new ArrayList<Student>();

        if (config.getLimit() > 0) {
            limit = Math.min(start + config.getLimit(), limit);
        }
        for (int i = config.getOffset(); i < limit; i++) {
            sublist.add(result.get(i));
        }
        session.getTransaction().commit();
        return new PagingLoadResultBean<Student>(sublist, result.size(), config.getOffset());
    }

    @Override
    public PagingLoadResult<StudentGroup> getStudentGroups(PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String sql = " from StudentGroup ";
        final Query query = session.createQuery(sql);
        List<StudentRole> result = query.list();
        PagingLoadResultBean<StudentGroup> bean = new PagingLoadResultBean(result, result.size(), 0);
        session.getTransaction().commit();
        return bean;

    }

    @Override
    public StudentGroup saveStudentGroup(StudentGroup group) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        StudentGroup savedGroup = (StudentGroup)session.merge(group);
        session.getTransaction().commit();
        return savedGroup;
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Teacher savedTeacher = (Teacher)session.merge(teacher);
        session.getTransaction().commit();
        return savedTeacher;
    }

    @Override
    public Student saveStudent(Student student) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Student savedStudent = (Student)session.merge(student);
        session.getTransaction().commit();
        return savedStudent;
    }

    @Override
    public void removeTeacher(Teacher teacher) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.delete(teacher);
        session.getTransaction().commit();
    }

    @Override
    public void removeStudent(Student student) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.delete(student);
        session.getTransaction().commit();
    }

    @Override
    public PagingLoadResult<TeacherRole> getTeacherRoles(PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String sql = " from TeacherRole ";
        final Query query = session.createQuery(sql);
        List<TeacherRole> result = query.list();
        PagingLoadResultBean<TeacherRole> bean = new PagingLoadResultBean(result, result.size(), 0);
        session.getTransaction().commit();
        return bean;
    }

    @Override
    public PagingLoadResult<StudentRole> getStudentRoles(PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String sql = " from StudentRole ";
        final Query query = session.createQuery(sql);
        List<StudentRole> result = query.list();
        PagingLoadResultBean<StudentRole> bean = new PagingLoadResultBean(result, result.size(), 0);
        session.getTransaction().commit();
        return bean;
    }


}
