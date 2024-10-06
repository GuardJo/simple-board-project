import ArticleCreateViewContainer from "@/containers/ArticleCreateViewContainer";
import { Meta, StoryObj } from "@storybook/react";

const meta = {
    title: "Containers/ArticleCreateViewContainer",
    component: ArticleCreateViewContainer,
} satisfies Meta<typeof ArticleCreateViewContainer>

export default meta;

type Story = StoryObj<typeof ArticleCreateViewContainer>

export const Default: Story = {
    args: {}
}