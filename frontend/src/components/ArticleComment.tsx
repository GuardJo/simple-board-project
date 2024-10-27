"use client"

import {Button} from "@headlessui/react";
import {CommentInfo} from "@/interface";
import {ChevronDownIcon, ChevronUpIcon, PencilSquareIcon, TrashIcon} from "@heroicons/react/16/solid";
import {useState} from "react";
import BasicButton from "@/components/BasicButton";
import {deleteComment, saveComment, updateComment} from "@/service/CommentService";
import {useRouter} from "next/navigation";
import {me} from "@/service/LoginService";
import DeleteActionDialog from "@/components/DeleteActionDialog";
import TextareaActionDialog from "@/components/TextareaActionDialog";

export default function ArticleComment({
                                           id,
                                           articleId,
                                           author,
                                           updatedAt,
                                           content,
                                           childComments = [],
                                           isOwner = false,
                                       }: ArticleCommentParam) {
    const [isExpanded, setIsExpanded] = useState(false);
    const [childCommentContent, setChildCommentContent] = useState("");
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
    const [targetId, setTargetId] = useState(id);
    const [openUpdateDialog, setOpenUpdateDialog] = useState(false);
    const [targetContent, setTargetContent] = useState(content);
    const router = useRouter();

    const handleSaveChildComment = async () => {
        await me()
            .then((check) => {
                if (check.ok) {
                    saveComment({
                        articleId: articleId,
                        content: childCommentContent,
                        parentCommentId: id,
                    })
                        .then((res) => {
                            if (res.ok) {
                                router.refresh()
                            } else {
                                throw new Error("Failed Create Comments");
                            }
                        })
                        .catch((error) => {
                            console.log(`Error : ${error}`);
                        });
                } else {
                    router.push("/login");
                }
            });
    }

    const handleOpenDeleteDialog = (commentId: number) => {
        setTargetId(commentId);
        setOpenDeleteDialog(true);
    }

    const handleRemoveComment = async () => {
        await deleteComment(targetId);
        setOpenDeleteDialog(false);
        router.refresh();
    }

    const handleCloseDeleteDialog = () => {
        setOpenDeleteDialog(false);
    }

    const handleOpenUpdateDialog = (commentId: number, content: string) => {
        setTargetId(commentId);
        setTargetContent(content);
        setOpenUpdateDialog(true);
    }

    const handleUpdateFormEvent = (changeContent: string) => {
        setTargetContent(changeContent);
    }

    const handleCloseUpdateDialog = () => {
        setOpenUpdateDialog(false);
    }

    const handleUpdateSubmit = async () => {
        await updateComment({commentId: targetId, content: targetContent});
        handleCloseUpdateDialog();
        router.refresh();
    }

    return (
        <div className="border-b border-gray-200 py-4">
            <div className="flex justify-between items-center mb-2">
                <div className="flex justify-center items-center gap-1">
                    <span className="font-semibold">{author}</span>
                    {isOwner ? <>
                        <Button onClick={() => handleOpenUpdateDialog(id, content)}
                                className="flex items-center text-sm text-gray-500 hover:text-gray-700">
                            <PencilSquareIcon className="h-4 w-4"/>
                        </Button>
                        <Button onClick={() => handleOpenDeleteDialog(id)}
                                className="flex items-center text-sm text-gray-500 hover:text-gray-700">
                            <TrashIcon className="mr-1 h-4 w-4"/>
                        </Button>
                    </> : null}
                </div>
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
                                <div className="flex justify-center items-center gap-2">
                                    <span className="font-semibold text-sm">{reply.creator}</span>
                                    {reply.isOwner ? <>
                                        <Button onClick={() => handleOpenUpdateDialog(reply.id, reply.content)}
                                                className="flex items-center text-xs text-gray-500 hover:text-gray-700">
                                            <PencilSquareIcon className="h-4 w-4"/>
                                        </Button>
                                        <Button onClick={() => handleOpenDeleteDialog(reply.id)}
                                                className="flex items-center text-xs text-gray-500 hover:text-gray-700">
                                            <TrashIcon className="mr-1 h-4 w-4"/>
                                        </Button>
                                    </> : null}
                                </div>
                                <span className="text-xs text-gray-500 cursor-help">
                          {reply.createTime}
                        </span>
                            </div>
                            <p className="text-sm">{reply.content}</p>
                        </div>
                    ))}
                    <textarea
                        placeholder="답글을 입력하세요..."
                        onChange={(event) => {
                            setChildCommentContent(event.target.value);
                        }}
                        className="w-full text-sm p-2"
                    />
                    <BasicButton onClick={handleSaveChildComment}>답글 작성</BasicButton>
                </div>
            )}
            <DeleteActionDialog dialogTitle="댓글 삭제" openDialog={openDeleteDialog} closeFn={handleCloseDeleteDialog}
                                deleteFn={handleRemoveComment}/>
            <TextareaActionDialog dialogTitle="댓글 수정" openDialog={openUpdateDialog} content={targetContent}
                                  formEventFn={handleUpdateFormEvent} closeFn={handleCloseUpdateDialog}
                                  submitFn={handleUpdateSubmit}/>
        </div>
    );
}

interface ArticleCommentParam {
    id: number,
    articleId: number,
    author: string,
    updatedAt: string,
    content: string,
    childComments?: CommentInfo[],
    isOwner?: boolean,
}