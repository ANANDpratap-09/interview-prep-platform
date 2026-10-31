import axiosInstance from "./axiosInstance";

export const register = (data) =>
    axiosInstance.post("/auth/register", data).then((res) => res.data);

export const login = (data) =>
    axiosInstance.post("/auth/login", data).then((res) => res.data);

export const getCurrentUser = () =>
    axiosInstance.get("/users/me").then((res) => res.data);