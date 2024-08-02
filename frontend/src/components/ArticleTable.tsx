import { ArticleList, HashtagInfo } from "@/interface";
import HashtagBadge from "./HashtagBadge";
import Link from "next/link";

export default function ArticleTable({ data }: Props = {}) {
    return (
        <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
            <table className="lg:w-[1000px] text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
                <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                    <tr>
                        <th scope="col" className="px-6 py-3 w-80">제목</th>
                        <th scope="col" className="px-6 py-3 w-20">해시태그</th>
                        <th scope="col" className="px-6 py-3 w-10">작성자</th>
                        <th scope="col" className="px-6 py-3 w-28">작성일시</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        (data === undefined || data.articles === undefined) ? null : data.articles.map((article) => (
                            <tr key={article.id} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
                                <td className="px-6 py-4">
                                    <Link href={`/articles/${article.id}`}>
                                        {article.title}
                                    </Link>
                                </td>
                                <td className="px-6 py-4">
                                    {(article.hashtags === undefined ? null : article.hashtags.map(hashtag => {
                                        return <HashtagBadge hashtagName={hashtag.hashtagName}></HashtagBadge>
                                    }))}
                                </td>
                                <td className="px-6 py-4 flex flex-wrap gap-1">{article.creator}</td>
                                <td className="px-6 py-4">{formatDate(article.createTime)}</td>
                            </tr>
                        ))}
                </tbody>
            </table>
        </div>
    );
}

function formatDate(date: string): string {
    return date
        .split(".")[0]
        .split("T")[0];
}

interface Props {
    data?: ArticleList,
};