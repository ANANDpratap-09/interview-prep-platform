import axios from "axios";

/**
 * One shared axios instance for the whole app. The request interceptor
 * below automatically attaches the JWT (if we have one) to every outgoing
 * request, so individual API calls never have to think about auth headers.
 */
const axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
});

axiosInstance.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// If the backend returns 401 (expired/invalid token), log the user out and
// send them to login. We skip this for /auth/ calls: a wrong password on
// the login form also returns 401, and that should show an error message
// on the form instead of reloading the page.
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        const isAuthCall = error.config?.url?.startsWith("/auth/");
        if (error.response?.status === 401 && !isAuthCall) {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;