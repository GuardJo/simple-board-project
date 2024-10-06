import Footer from "@/components/Footer";
import { Meta, StoryObj } from "@storybook/react";

const meta = {
    title: "components/Footer",
    component: Footer,
} satisfies Meta<typeof Footer>

export default meta;
type Story = StoryObj<typeof Footer>;

export const Default: Story = {
    args: {},
};