import { ArticleCreateRequest } from "@/interface";

const baseUrl = "http://localhost:8080/api/v2";

export async function createArticle(createRequest : ArticleCreateRequest) {
    await fetch(`${baseUrl}/articles`, {
        method: "POST",
        credentials: "include",
        cache: 'no-store',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(createRequest),
    });
}