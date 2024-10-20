import {CommentCreateRequest} from "@/interface";

const baseUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

export async function saveComment(request: CommentCreateRequest): Promise<Response> {
    return await fetch(`${baseUrl}/comments`, {
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
        method: "POST",
        body: JSON.stringify(request)
    })
}