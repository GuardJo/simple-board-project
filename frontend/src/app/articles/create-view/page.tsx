import { Metadata } from "next";

export const metadata: Metadata = {
    title: "게시글 작성",
};

export default function CreateView() {
    return (
        <div className="flex justify-center content-start gap-3 px-24 py-10">
            <h3>게시글 작성 페이지</h3>
        </div>
    );
}