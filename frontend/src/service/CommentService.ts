import {CommentCreateRequest, CommentUpdateRequest} from "@/interface";

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

export async function deleteComment(commentId: number): Promise<void> {
    await fetch(`${baseUrl}/comments/${commentId}`, {
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
        method: "DELETE"
    }).then((res) => {
        if (res.ok) {
            return;
        } else {
            throw new Error(res.statusText);
        }
    });
}

export async function updateComment(updateRequest: CommentUpdateRequest): Promise<void> {
    await fetch(`${baseUrl}/comments`, {
        method: "PATCH",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(updateRequest),
    })
        .then((res) => {
            if (res.ok) {
                return;
            } else {
                throw new Error(res.statusText);
            }
        });
}