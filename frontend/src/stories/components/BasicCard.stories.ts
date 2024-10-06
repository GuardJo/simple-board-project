import BasicCard from "@/components/BasicCard";
import { Meta, StoryObj } from "@storybook/react";

const meta = {
    title: "components/BasicCard",
    component: BasicCard,
} satisfies Meta<typeof BasicCard>

export default meta;
type Story = StoryObj<typeof BasicCard>;

export const Default: Story = {
    args: {},
};

export const HasChildren: Story = {
    args: {
        title: "Test Title",
        children: "Test~~",
    },
};