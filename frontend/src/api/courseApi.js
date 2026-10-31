import axiosInstance from "./axiosInstance";

export const browseCourses = (topic, difficulty) =>
    axiosInstance
        .get("/courses", { params: { topic, difficulty } })
        .then((res) => res.data);

export const getCourseDetail = (id) =>
    axiosInstance.get(`/courses/${id}`).then((res) => res.data);

export const enrollInCourse = (id) =>
    axiosInstance.post(`/courses/${id}/enroll`).then((res) => res.data);

export const completeLesson = (lessonId) =>
    axiosInstance.post(`/lessons/${lessonId}/complete`).then((res) => res.data);
