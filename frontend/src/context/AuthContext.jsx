import { createContext, useContext, useState } from "react";
import * as authApi from "../api/authApi";

/**
 * Holds the logged-in user's info and token across the whole app.
 * Any component can read `user` or call `login`/`logout` via useAuth(),
 * instead of passing props down through every page.
 */
const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    // Initialize from localStorage so a page refresh doesn't log the user out.
    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem("user");
        return stored ? JSON.parse(stored) : null;
    });

    const login = async (email, password) => {
        const data = await authApi.login({ email, password });
        persistSession(data);
        return data;
    };

    const register = async (name, email, password) => {
        const data = await authApi.register({ name, email, password });
        persistSession(data);
        return data;
    };

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setUser(null);
    };

    // Shared by login/register: save the token and user shape, and update state.
    const persistSession = (data) => {
        localStorage.setItem("token", data.token);
        const userInfo = { name: data.name, email: data.email, role: data.role };
        localStorage.setItem("user", JSON.stringify(userInfo));
        setUser(userInfo);
    };

    return (
        <AuthContext.Provider value={{ user, login, register, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

// Custom hook so components write `const { user } = useAuth();`
// instead of importing useContext + AuthContext everywhere.
export function useAuth() {
    return useContext(AuthContext);
}