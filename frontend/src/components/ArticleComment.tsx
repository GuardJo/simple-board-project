"use client"

import {Button} from "@headlessui/react";
import {CommentInfo} from "@/interface";
import {ChevronDownIcon, ChevronUpIcon} from "@heroicons/react/16/solid";
import {useState} from "react";
import BasicButton from "@/components/BasicButton";

export default function ArticleComment({id, author, updatedAt, content, childComments = []}: ArticleCommentParam) {
    const [isExpanded, setIsExpanded] = useState(false);

    return (
        <div className="border-b border-gray-200 py-4">
            <div className="flex justify-between items-center mb-2">
                <span className="font-semibold">{author}</span>
                <span className="text-sm text-gray-500 cursor-help">
                  {updatedAt}
                </span>
            </div>
            <p className="mb-2">{content}</p>
            <Button
                onClick={() => setIsExpanded(!isExpanded)}
                aria-expanded={isExpanded}
                aria-controls={`replies-${id}`}
                className="flex items-center text-sm text-gray-500 hover:text-gray-700"
            >
                {isExpanded ? <ChevronUpIcon className="mr-1 h-4 w-4"/> :
                    <ChevronDownIcon className="mr-1 h-4 w-4"/>}
                {(childComments?.length === 0) ? '답글 작성' : `${childComments.length}개의 답글`}
            </Button>
            {isExpanded && (
                <div id={`replies-${id}`} className="mt-2 ml-4 space-y-2">
                    {childComments?.map(reply => (
                        <div key={reply.id} className="border-t border-gray-100 pt-2">
                            <div className="flex justify-between items-center mb-1">
                                <span className="font-semibold text-sm">{reply.creator}</span>
                                <span className="text-xs text-gray-500 cursor-help">
                          {reply.createTime}
                        </span>
                            </div>
                            <p className="text-sm">{reply.content}</p>
                        </div>
                    ))}
                    <form onSubmit={(e) => {
                        e.preventDefault()
                        // TODO API 연동
                        console.log("댓글 저장");
                    }} className="mt-2">
                        <textarea
                            placeholder="답글을 입력하세요..."
                            onChange={() => {
                            }}
                            className="w-full text-sm p-2"
                        />
                        <BasicButton onClick={() => {
                        }}>답글 작성</BasicButton>
                    </form>
                </div>
            )}
        </div>
    );
}

interface ArticleCommentParam {
    id: number,
    author: string,
    updatedAt: string,
    content: string,
    childComments?: CommentInfo[],
}