import ArticleComment from "@/components/ArticleComment";
import {Meta, StoryObj} from "@storybook/react";

const meta = {
    title: "components/ArticleComment",
    component: ArticleComment,
} satisfies Meta<typeof ArticleComment>;

export default meta;
type Story = StoryObj<typeof ArticleComment>;

export const Default: Story = {
    args: {
        id: 1,
        author: "테스터",
        updatedAt: "2024-07-28 23:55",
        content: "테스트 댓글"
    },
};

export const MyComments: Story = {
    args: {
        id: 1,
        author: "테스터",
        updatedAt: "2024-07-28 23:55",
        content: "테스트 댓글",
        childComments: [],
        isOwner: true,
    },
}

export const HasChildComments: Story = {
    args: {
        id: 1,
        author: "테스터",
        updatedAt: "2024-07-28 23:55",
        content: "테스트 댓글",
        childComments: [
            {
                id: 2,
                creator: 'Tester2',
                createTime: '2024-10-13 23:00',
                content: 'Sub Comment',
                childComments: [],
            },
        ],
    },
};