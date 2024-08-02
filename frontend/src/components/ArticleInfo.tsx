import HashtagBadge from "./HashtagBadge";

export default function ArticleInfo({ author, createdTime, hashtagNames }: ArticleInfoParams) {
    return (
        <dl className="max-w-md text-gray-900 divide-y divide-gray-200 dark:text-white dark:divide-gray-700">
            <div className="flex flex-col pb-3">
                <dt className="mb-1 text-gray-500 md:text-md dark:text-gray-400">작성자</dt>
                <dd className="text-md font-semibold">{author}</dd>
            </div>
            <div className="flex flex-col py-3">
                <dt className="mb-1 text-gray-500 md:text-md dark:text-gray-400">작성 시각</dt>
                <dd className="text-md font-semibold">{createdTime}</dd>
            </div>
            <div className="flex flex-col pt-3">
                <dt className="mb-1 text-gray-500 md:text-md dark:text-gray-400">해시태그</dt>
            </div>
            {(hashtagNames === undefined || hashtagNames.length == 0) ? null : hashtagNames.map(hashtagName => (
                <HashtagBadge hashtagName={hashtagName} />
            ))}
        </dl>
    );
}

interface ArticleInfoParams {
    author: string,
    createdTime: string,
    hashtagNames?: string[],
}