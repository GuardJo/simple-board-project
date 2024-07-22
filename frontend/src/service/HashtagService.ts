import { HashtagInfo } from "@/interface";

const baseUrl = "http://localhost:8080/api/v2";

export function getHashTagList() : HashtagInfo[]{
    // TODO API 연동하기
    return [
        {
            id: 1,
            hashtagName: "tag1",
        },
        {
            id: 2,
            hashtagName: "tag2",
        },
        {
            id: 3,
            hashtagName: "tag3",
        },
        {
            id: 4,
            hashtagName: "tag4",
        },
        {
            id: 5,
            hashtagName: "tag5",
        },
    ];
}