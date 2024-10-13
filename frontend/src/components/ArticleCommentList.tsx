import {CommentInfo} from "@/interface";
import BasicButton from "@/components/BasicButton";
import ArticleComment from "@/components/ArticleComment";

export default function ArticleCommentList({data = []}: ArticleCommentListParams = {}) {
    return (
        <div className="max-w-2xl mx-auto p-4">
            <div className="mb-8">
                <textarea
                    placeholder="댓글을 입력하세요..."
                    onChange={(e) => {
                    }}
                    className="w-full p-2"
                />
                <BasicButton>댓글 작성</BasicButton>
            </div>
            <div className="space-y-4">
                {(data?.length === 0) ? '등록된 댓글이 없습니다.' : data.map(comment => (
                    <ArticleComment key={comment.id} id={comment.id} author={comment.creator} content={comment.content}
                                    updatedAt={comment.createTime} childComments={comment.childComments}/>
                ))}
            </div>
        </div>);
}

interface ArticleCommentListParams {
    data?: CommentInfo[],
};