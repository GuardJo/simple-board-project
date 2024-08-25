import { LoginRequest } from "@/interface";

const baseUrl = "http://localhost:8080/api/v2";

export async function login(loginRequest: LoginRequest) {
    console.log(`username : ${loginRequest.username}, password : ${loginRequest.password}`);

    let loginParams = new URLSearchParams({
        "username": loginRequest.username,
        "password": loginRequest.password,
    });

    const response = await fetch(`${baseUrl}/login`, {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: loginParams,
    })
    .then(res => {
        if (res.ok) {
            console.log("Login Successes");
            return true;
        } else {
            console.log(`Login Response : ${res.body}`);
            return false;
        }
    })
    .catch(e => {
        console.log(`Error : ${e}`);
        return false;
    });

    return response;
}