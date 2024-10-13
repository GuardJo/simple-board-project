import ArticleUpdateFormContainer from "@/containers/ArticleUpdateFormContainer";
import { Meta, StoryObj } from "@storybook/react";

const meta = {
    title: "Containers/ArticleUpdateFormContainer",
    component: ArticleUpdateFormContainer,
} satisfies Meta<typeof ArticleUpdateFormContainer>;

export default meta;

type Story = StoryObj<typeof ArticleUpdateFormContainer>

export const UpdateView: Story = {
    args: {
        articleId: 1,
        title: "Test Title",
        content: "Test Content",
    }
};

export const CreateView: Story = {
    args: {}
};