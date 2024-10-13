const baseUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

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