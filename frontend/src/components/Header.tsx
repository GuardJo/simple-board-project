"use client";

import { Bars3Icon } from "@heroicons/react/24/outline";
import Image from "next/image";
import Link from "next/link";
import { useState } from "react";

export default function Header() {
    const [isLogin, setIsLogin] = useState(false);
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

    return (
        <header className="bg-white">
            <nav className="mx-auto flex max-w-7xl items-center justify-between p-6 lg:px-8" aria-label="Global">
                <div className="flex lg:flex-1">
                    <Link href='/' className="-m-1.5 p-1.5">
                        <span className="h-9 w-auto">Simple Board</span>
                    </Link>
                </div>
                <div className="hidden lg:flex lg:gap-x-12">
                    <Link href='/' className="text-sm font-semibold leading-6 text-gray-900">Home</Link>
                    <Link href='/search-hashtag' className="text-sm font-semibold leading-6 text-gray-900">Hashtags</Link>
                </div>
                <div className="hidden lg:flex lg:flex-1 lg:justify-end gap-5">
                    {(!isLogin) ?
                        <>
                            <Link href='/login' onClick={() => setIsLogin(true)} className="text-sm font-semibold leading-6 text-gray-900">Login</Link>
                            <Image alt='kakao login' src='/images/kakao_login_small.png' width={50} height={20} />
                        </> :
                        <Link href='/logout' onAbort={() => setIsLogin(false)} className="text-sm font-semibold leading-6 text-gray-900">Logout</Link>
                    }
                </div>
            </nav>
        </header>
    )
}