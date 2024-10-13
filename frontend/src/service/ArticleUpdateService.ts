import {ArticleCreateRequest, ArticleUpdateRequest} from "@/interface";

const baseUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

export async function createArticle(createRequest: ArticleCreateRequest) {
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

export async function updateArticle(updateRequest: ArticleUpdateRequest): Promise<void> {
    await fetch(`${baseUrl}/articles`, {
        method: 'PATCH',
        credentials: 'include',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updateRequest),
    });
}