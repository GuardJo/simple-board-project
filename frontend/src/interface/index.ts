export interface ArticleList {
    number: number,
    totalPage: number,
    articles: ArticleInfo[],
};

export interface ArticleInfo {
    id: number,
    title: string,
    content: string,
    creator: string,
    hashtags: HashtagInfo[],
    createTime: string,
};

export interface HashtagInfo {
    id: number,
    hashtagName: string,
}

export interface CommentInfo {
    id: number,
    creator: string,
    content: string,
    createTime: string,
}

export interface ArticleDetailInfo {
    article: ArticleInfo,
    comments: CommentInfo[],
    isOwner: Boolean,
}

export interface ArticleCreateRequest {
    title: string,
    content: string,
}

export interface LoginRequest {
    username: string,
    password: string,
}

export interface ArticleUpdateRequest {
    id: number,
    title: string,
    content: string,
}