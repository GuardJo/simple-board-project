import ArticleCreateViewContainer from "@/containers/ArticleCreateViewContainer";
import { Metadata } from "next";

export const metadata: Metadata = {
    title: "신규 게시글 작성"
}

export default function CreateView() {
    return(
        <ArticleCreateViewContainer/>
    )
}