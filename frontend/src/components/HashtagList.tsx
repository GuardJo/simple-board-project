import { HashtagInfo } from "@/interface";
import Link from "next/link";
import HashtagBadge from "./HashtagBadge";

export default function HashtagList({ data }: Props = {}) {
    return (
        <>
            <div>
                <span className="h-9 w-auto font-bold sm:text-xl">Hashtags</span>
            </div>
            <div className="flex gap-1 flex-wrap lg:w-[1000px]">
                {
                    (data === undefined || data === null
                        ? <p className="text-base font-semibold text-black-600">No Tags</p>
                        : data.map(hashtagInfo => {
                            return <Link key={hashtagInfo.id} href={`/articles/search-hashtags?searchValue=${hashtagInfo.hashtagName}`}>
                                <HashtagBadge key={hashtagInfo.id} hashtagName={hashtagInfo.hashtagName}></HashtagBadge>
                            </Link>
                        }))
                }
            </div>
        </>
    );
}

interface Props {
    data?: HashtagInfo[],
};