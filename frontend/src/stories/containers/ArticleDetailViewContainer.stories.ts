import ArticleDetailViewContainer from "@/containers/ArticleDetailViewContainer";
import {Meta, StoryObj} from "@storybook/react";

const meta = {
    title: "Containers/ArticleDetailViewContainer",
    component: ArticleDetailViewContainer,
} satisfies Meta<typeof ArticleDetailViewContainer>

export default meta;

type Story = StoryObj<typeof ArticleDetailViewContainer>;

export const Default: Story = {
    args: {
        articleId: 1,
        articleDetail: {
            article: {
                id: 1,
                title: "Test Article",
                content: "Test Content",
                createTime: "2024-03-03",
                creator: "Tester",
                hashtags: [],
            },
            comments: [],
            isOwner: false,
        }
    }
};

export const isOwner: Story = {
    args: {
        articleId: 1,
        articleDetail: {
            article: {
                id: 1,
                title: "Test Article",
                content: "Test Content",
                createTime: "2024-03-03",
                creator: "Tester",
                hashtags: [],
            },
            comments: [],
            isOwner: true,
        }
    }
};

export const HasComments: Story = {
    args: {
        articleId: 1,
        articleDetail: {
            article: {
                id: 1,
                title: "Test Article",
                content: "Test Content",
                createTime: "2024-03-03",
                creator: "Tester",
                hashtags: [],
            },
            comments: [
                {
                    id: 1,
                    content: "Test Comment",
                    creator: "Tester2",
                    createTime: "2024-03-03",
                    childComments: [],
                },
                {
                    id: 2,
                    content: "Test Comment2",
                    creator: "Tester3",
                    createTime: "2024-03-03",
                    childComments: [],
                },
            ],
            isOwner: false,
        }
    }
}