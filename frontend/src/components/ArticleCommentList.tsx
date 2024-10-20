"use client"

import {CommentInfo} from "@/interface";
import BasicButton from "@/components/BasicButton";
import ArticleComment from "@/components/ArticleComment";
import {useState} from "react";
import {saveComment} from "@/service/CommentService";
import {useRouter} from "next/navigation";
import {me} from "@/service/LoginService";

export default function ArticleCommentList({articleId, data = []}: ArticleCommentListParams) {
    const [commentContent, setCommentContent] = useState('');
    const router = useRouter();

    const handleSaveComment = async () => {
        await me()
            .then((check) => {
                if (check.ok) {
                    saveComment({
                        articleId: articleId,
                        content: commentContent
                    })
                        .then((res) => {
                            if (res.ok) {
                                router.refresh();
                            } else {
                                throw new Error("Failed Create Comment");
                            }
                        })
                        .catch((error) => {
                            console.log(`Error : ${error}`);
                        })
                } else {
                    router.push("/login");
                }
            })
    }
    return (
        <div className="max-w-2xl mx-auto p-4">
            <div className="mb-8">
                <textarea
                    placeholder="댓글을 입력하세요..."
                    onChange={(e) => {
                        setCommentContent(e.target.value);
                    }}
                    className="w-full p-2"
                />
                <BasicButton onClick={handleSaveComment}>댓글 작성</BasicButton>
            </div>
            <div className="space-y-4">
                {(data?.length === 0) ? '등록된 댓글이 없습니다.' : data.map(comment => (
                    <ArticleComment key={comment.id} id={comment.id} articleId={articleId} author={comment.creator}
                                    content={comment.content}
                                    updatedAt={comment.createTime} childComments={comment.childComments}/>
                ))}
            </div>
        </div>);
}

interface ArticleCommentListParams {
    articleId: number,
    data?: CommentInfo[],
}