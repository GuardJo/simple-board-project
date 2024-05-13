const baseUrl = "http://localhost:8080/api/v2"

export async function getArticlePage() {
    const response = await fetch(`${baseUrl}/articles`);

    return response.json();
}