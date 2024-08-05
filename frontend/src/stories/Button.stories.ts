import { Meta, StoryObj } from "@storybook/react";
import Button from './../components/Button';

const meta = {
    title: "components/Button",
    component: Button,
} satisfies Meta<typeof Button>

export default meta;

type Story = StoryObj<typeof Button>

export const noUrl: Story = {
    args: {
        children: "No Url",
    },
};

export const hasUrl: Story = {
    args: {
        children: "Naver",
        url: "https://naver.com",
    },
};