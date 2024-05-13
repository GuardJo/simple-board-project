import { ArticleList } from "@/interface";

export default ({ data }: Props = {}) => {
    return (
        <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
            <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
                <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                    <tr>
                        <th scope="col" className="px-6 py-3 w-96">제목</th>
                        <th scope="col" className="px-6 py-3 w-10">해시태그</th>
                        <th scope="col" className="px-6 py-3 w-10">작성자</th>
                        <th scope="col" className="px-6 py-3 w-16">작성일시</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        (data === undefined || data._embedded === undefined) ? null : data._embedded.map((article) => (
                            <tr className="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
                                <td className="px-6 py-4">{article.title}</td>
                                <td className="px-6 py-4">Tag</td>
                                <td className="px-6 py-4">{article.creator}</td>
                                <td className="px-6 py-4">{article.createtime}</td>
                            </tr>
                        ))}
                </tbody>
            </table>
        </div>
    );
}

interface Props {
    data?: ArticleList,
};