import axiosInstance from "./axiosInstance";

export const getDashboard = () =>
    axiosInstance.get("/progress/dashboard").then((res) => res.data);