package com.fotius.server.impl;

import com.fotius.client.service.FotiusService;
import com.fotius.server.impl.hibernate.HibernateUtil;
import com.fotius.shared.model.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.*;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

public class FotiusServiceImpl extends RemoteServiceServlet implements FotiusService {

    private HibernateUtil hibernateUtil = new HibernateUtil();
    private static Logger log = Logger.getLogger(FotiusServiceImpl.class.getName());

    @Override
    public User login(String login, String password) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            log.info("Logging in user with credentials " + login + " " + password);
            String sql = " from User where login = '" + login + "' and password = '" + password + "'";

            Query query = session.createQuery(sql);
            if (query.list().size() == 0) {
                return null;
            } else {
                return (User)query.list().get(0);
            }
        } catch (Exception e) {
            log.error("We've got a problem while loggin in. User credentials: " + login + " " + password);
            e.printStackTrace();
            return null;
        }
        finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public User register(User user) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            User savedUser = null;
            log.info("Creating user with credentials " + user.getLogin() + " " + user.getPassword());
            if (user.getRole() instanceof TeacherRole) {
                Teacher toSave = new Teacher();
                toSave.setLogin(user.getLogin());
                toSave.setPassword(user.getPassword());
                toSave.setRole(user.getRole());
                return (Teacher)session.merge(toSave);
            } else {
                Student toSave = new Student();
                toSave.setLogin(user.getLogin());
                toSave.setPassword(user.getPassword());
                //TODO complete and forget
                user = saveStudent((Student) user);
            }
            return savedUser;

        } catch (Exception e) {
            log.error("We've got a problem while registering in. User credentials: " + user.getLogin() + " " + user.getPassword());
            e.printStackTrace();
            return null;
        }

        finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public PagingLoadResult<Teacher> getTeachers(PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
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
            return new PagingLoadResultBean<Teacher>(sublist, records.size(), config.getOffset());
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public PagingLoadResult<Student> getStudents(PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
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
            return new PagingLoadResultBean<Student>(sublist, result.size(), config.getOffset());
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getUsers() {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            String sql = " from User ";
            final Query query = session.createQuery(sql);
            List<User> result = query.list();
            return result;
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public PagingLoadResult<StudentGroup> getStudentGroups(PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            String sql = " from StudentGroup ";
            final Query query = session.createQuery(sql);
            List<StudentRole> result = query.list();
            PagingLoadResultBean<StudentGroup> bean = new PagingLoadResultBean(result, result.size(), 0);
            return bean;
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public StudentGroup saveStudentGroup(StudentGroup group) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            StudentGroup savedGroup = (StudentGroup)session.merge(group);
            return savedGroup;
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeGroup(StudentGroup group) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.delete(group);
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Teacher savedTeacher = (Teacher)session.merge(teacher);
            return savedTeacher;
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public Student saveStudent(Student student) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Student savedStudent = (Student)session.merge(student);
            return savedStudent;
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeTeacher(Teacher teacher) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.delete(teacher);
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeStudent(Student student) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.delete(student);
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public PagingLoadResult<TeacherRole> getTeacherRoles(PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            String sql = " from TeacherRole ";
            final Query query = session.createQuery(sql);
            List<TeacherRole> result = query.list();
            PagingLoadResultBean<TeacherRole> bean = new PagingLoadResultBean(result, result.size(), 0);
            return bean;
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public PagingLoadResult<StudentRole> getStudentRoles(PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            String sql = " from StudentRole ";
            final Query query = session.createQuery(sql);
            List<StudentRole> result = query.list();
            PagingLoadResultBean<StudentRole> bean = new PagingLoadResultBean(result, result.size(), 0);
            return bean;
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public Message sendMessage(Message msg) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(msg);
            return new Message();//TODO remove this bullshit
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public PagingLoadResult<Message> getInbox(User user, PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            String sql = " from Message where recipient_id = " + user.getUserId();
            final Query query = session.createQuery(sql);
            List<Message> result = query.list();
            PagingLoadResultBean<Message> bean = new PagingLoadResultBean(result, result.size(), 0);
            return bean;
        } finally {
            session.getTransaction().commit();
        }
    }

    @Override
    public PagingLoadResult<Message> getOutbox(User user, PagingLoadConfig config) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            String sql = " from Message where senderId = " + user.getUserId();
            final Query query = session.createQuery(sql);
            List<Message> result = query.list();
            PagingLoadResultBean<Message> bean = new PagingLoadResultBean(result, result.size(), 0);
            return bean;
        } finally {
            session.getTransaction().commit();
        }
    }


}
