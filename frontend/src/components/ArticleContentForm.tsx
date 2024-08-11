"use client"

export default function ArticleContentForm({ content, canWrite, setContent }: ArticleContentProps) {
    return (
        <div>
            <label htmlFor="message" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">본문</label>
            <textarea id="message" rows={10} placeholder="게시글 내용을 입력하시오." readOnly={!canWrite} onChange={() => setContent} className="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
                {content}
            </textarea>
        </div>
    )
}

interface ArticleContentProps {
    content: string,
    canWrite?: boolean,
    setContent?: Function,
};