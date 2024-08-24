import { LoginRequest } from "@/interface";

const baseUrl = "http://localhost:8080/api/v2";

export function login(loginRequest: LoginRequest) {
    console.log(`username : ${loginRequest.username}, password : ${loginRequest.password}`);
}