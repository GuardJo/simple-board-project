export default function ArticleComment({ author, updatedAt, content }: ArticleCommentParam) {
    return (
        <div className="flex flex-col py-3">
            <dt className="flex gap-1 justify-between">
                <p className="mb-1 text-gray-500 md:text-md dark:text-gray-400">{author}</p>
                <p className="mb-1 text-gray-400 md:text-xs dark:text-gray-300">{updatedAt}</p>
            </dt>
            <dd className="text-sm font-semibold">{content}</dd>
        </div>
    )
}

interface ArticleCommentParam {
    author: String,
    updatedAt: String,
    content: String,
};