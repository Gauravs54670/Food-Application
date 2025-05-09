import axios from "axios";

const API_URL = "http://localhost:8080/api";

export const registerUser = async (data) => {
    try {
        const response = await axios.post(
            API_URL+"/register",
            data
          );
        return response
    } catch (error) {
        throw error;
    }
}

export const login = async (data) => {
    try {
        // ✅ Map username field (frontend) ➔ username field (backend expects)
        const payload = {
            username: data.username,
            password: data.password,
        };
        const response = await axios.post(API_URL + "/login", payload);
        return response;
    } catch (error) {
        throw error;
    }
}
