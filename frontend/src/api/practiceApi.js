import axiosInstance from "./axiosInstance";

export const startSession = (data) =>
    axiosInstance.post("/practice/sessions", data).then((res) => res.data);

export const submitAnswer = (sessionId, data) =>
    axiosInstance
        .post(`/practice/sessions/${sessionId}/answers`, data)
        .then((res) => res.data);

export const finishSession = (sessionId) =>
    axiosInstance
        .post(`/practice/sessions/${sessionId}/finish`)
        .then((res) => res.data);