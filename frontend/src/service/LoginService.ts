import {LoginRequest} from "@/interface";

const baseUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

export async function login(loginRequest: LoginRequest) {
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
                return true;
            } else {
                return false;
            }
        })
        .catch(e => {
            console.log(`Error : ${e}`);
            return false;
        });

    return response;
}

export async function me() {
    const response = await fetch(`${baseUrl}/me`, {
        method: "GET",
        cache: "no-store",
        credentials: "include",
    })

    return response;
}

export async function logout() {
    const response = await fetch(`${baseUrl}/logout`, {
        method: 'POST',
        cache: 'no-store',
        credentials: 'include',
    });

    return response;
}