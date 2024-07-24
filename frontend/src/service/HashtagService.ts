
const baseUrl = "http://localhost:8080/api/v2";

export async function getHashTagList() {
    const response = await fetch(`${baseUrl}/hashtags`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        }
    );

    return response.json();
}