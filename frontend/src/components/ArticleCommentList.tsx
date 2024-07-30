import { CommentInfo } from "@/interface";
import ArticleComment from "./ArticleComment";

export default function ArticleCommentList({ data }: ArticleCommentListParams = {}) {
    return (
        <div>
            <dl className="w-full text-gray-900 divide-y divide-gray-200 dark:text-white dark:divide-gray-700">
                {(data === undefined ? <p>등록된 댓글이 없습니다.</p> : data.map((comment) => (
                    <div className="flex flex-col">
                        <ArticleComment author={comment.author} content={comment.content} updatedAt={comment.createdTime}></ArticleComment>
                    </div>
                )))}
            </dl>
        </div>
    );
}

interface ArticleCommentListParams {
    data?: CommentInfo[],
};